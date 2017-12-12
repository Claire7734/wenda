package com.project.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.project.model.User;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

/**
 * Created by Claire on 12/12/17.
 */
public class JedisAdapter {

    private static void print(int index, Object obj) {
        System.out.println(String.format("%d, %s", index, obj.toString()));
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis("redis://localhost:6379/9");
        jedis.flushDB();

        jedis.set("hello", "world");
        jedis.rename("hello", "newhello");
        jedis.setex("hello2", 15, "world"); //过期时间15秒

        jedis.set("pv", "100");
        jedis.incr("pv");
        jedis.incrBy("pv", 5);
        jedis.decrBy("pv", 3);

        print(3, jedis.keys("*"));

        //list 插入弹出类似栈操作 lpush lpop
        String listName = "list";
        jedis.del(listName);
        for (int i = 0; i < 10; i++) {
            jedis.lpush(listName, "a" + String.valueOf(i)); //插入list
        }
        print(4, jedis.lrange(listName, 0, 12)); //lrange 范围 倒序显示数据 [a9, a8, a7, a6, a5, a4, a3, a2, a1, a0]
        print(4, jedis.lrange(listName, 0, 3)); // [a9, a8, a7, a6]
        print(5, jedis.llen(listName)); //长度
        print(6, jedis.lpop(listName)); // 弹出
        print(7, jedis.llen(listName));
        print(8, jedis.lindex(listName, 3));
        jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER, "a4", "xx");
        print(10, jedis.lrange(listName, 0, 12));

        //hash
        String userKey = "userxx";
        jedis.hset(userKey, "name", "jim");
        jedis.hset(userKey, "age", "12");
        print(12, jedis.hget(userKey, "name")); //12, jim
        print(13, jedis.hgetAll(userKey)); //13, {name=jim, age=12}
        jedis.hdel(userKey, "age");
        print(14, jedis.hgetAll(userKey));
        print(15, jedis.hexists(userKey, "email"));//containsKey方法
        print(16, jedis.hexists(userKey, "name"));
        print(17, jedis.hkeys(userKey));
        print(18, jedis.hvals(userKey));
        jedis.hsetnx(userKey, "school", "zju");
        jedis.hsetnx(userKey, "name", "aa"); //不存在插入
        print(19, jedis.hgetAll(userKey)); //{school=zju, name=jim}

        //set 集合
        String likeKey1 = "commentLike1";
        String likeKey2 = "commentLike2";
        for (int i = 0; i < 10; i++) {
            jedis.sadd(likeKey1, String.valueOf(i));
            jedis.sadd(likeKey2, String.valueOf(i * i));
        }
        print(20, jedis.smembers(likeKey1)); //20, [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
        print(21, jedis.smembers(likeKey2)); //21, [0, 1, 4, 9, 16, 25, 36, 49, 64, 81]
        print(22, jedis.sunion(likeKey1, likeKey2)); //集合并 22, [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 16, 25, 36, 49, 64, 81]
        print(23, jedis.sdiff(likeKey1, likeKey2)); //第一个集合有，第二个没有 23, [8, 2, 3, 5, 6, 7]
        print(24, jedis.sinter(likeKey1, likeKey2)); //集合交 24, [0, 1, 4, 9]
        print(25, jedis.sismember(likeKey1, "12")); //25, false
        jedis.srem(likeKey1, "5"); //删除5 rem
        print(27, jedis.smembers(likeKey1));
        jedis.smove(likeKey2, likeKey1, "25"); //从集合1向集合2移动元素
        print(28, jedis.smembers(likeKey1)); // 28, [0, 1, 2, 3, 4, 6, 7, 8, 9, 25]
        print(29, jedis.scard(likeKey1)); //元素总数量 29, 10

        //优先队列 Sorted Sets
        String rankKey = "rankKey";
        jedis.zadd(rankKey, 15, "jim");
        jedis.zadd(rankKey, 80, "Lucy");
        jedis.zadd(rankKey, 60, "Ben");
        print(30, jedis.zcard(rankKey)); //30, 3
        print(31, jedis.zcount(rankKey, 60, 100)); //31, 2
        print(32, jedis.zscore(rankKey, "Lucy")); //32, 80.0    浮点数
        jedis.zincrby(rankKey, 2, "Lucy");
        print(33, jedis.zscore(rankKey, "Lucy")); //33, 82.0
        jedis.zincrby(rankKey, 2, "Luc");
        print(34, jedis.zscore(rankKey, "Luc")); //34, 2.0
        print(35, jedis.zrange(rankKey, 0, 10)); //35, [Luc, jim, Ben, Lucy] 默认从小到大排序
        print(36, jedis.zrevrange(rankKey, 0, 10)); //36, [Lucy, Ben, jim, Luc]
        for (Tuple tuple : jedis.zrangeByScoreWithScores(rankKey, 60, 100)) {
            print(37, tuple.getElement() + ":" + String.valueOf(tuple.getScore()));
        } //37, Ben:60.0    37, Lucy:82.0
        print(38, jedis.zrank(rankKey, "Ben")); //排名 38, 2
        print(39, jedis.zrevrank(rankKey, "Ben")); // 39, 1

        String setKey = "zset";
        jedis.zadd(setKey, 1, "c");
        jedis.zadd(setKey, 1, "a");
        jedis.zadd(setKey, 1, "b");
        print(40, jedis.zlexcount(setKey, "-", "+")); //40, 3 范围在负无穷到正无穷
        print(41, jedis.zlexcount(setKey, "[b", "[d")); // 41, 2
        print(42, jedis.zlexcount(setKey, "(b", "(d")); //42, 1
        jedis.zremrangeByLex(setKey, "(b", "+"); //删除b以上
        print(44, jedis.zrange(setKey, 0, 10)); //44, [a, b]


        JedisPool pool = new JedisPool("redis://localhost:6379/9");
        for (int i = 0; i < 100; i++) {
            Jedis j = pool.getResource();
            print(45, j.get("pv"));
            j.close(); //需要关闭！！！！
        }

        // 用redis缓存
        User user = new User();
        user.setName("xxx");
        user.setPassword("xxx");
        jedis.set("user1", JSONObject.toJSONString(user));
        print(46, jedis.get("user1")); // 46, {"name":"xxx","password":"xxx","userId":0}

        String str = jedis.get("user1");
        User user2 = JSON.parseObject(str, User.class);
    }


}
