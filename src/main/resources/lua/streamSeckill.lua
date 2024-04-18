-- 加锁
local key = KEYS[1];
-- 订单id
local orderId = ARGV[1];
-- 用户id
local userId = ARGV[2];
-- 活动id
local activityId = ARGV[3];
-- 分组
local group = ARGV[4];

-- 异常处理方法
local function errorHandler(err)
    redis.log(redis.LOG_NOTICE, "创建分组失败："..err);
    print("创建分组失败:"..err)
end

-- 创建分组
local status = xpcall(function()
    redis.call('XGROUP', 'CREATE', key, group, '$', 'MKSTREAM');
    redis.log(redis.LOG_NOTICE, "创建分组成功："..group);
    print("创建分组成功:"..group)
end, errorHandler)

-- 发送消息 XADD key * k1 v1 k2 v2 ...
redis.log(redis.LOG_NOTICE, "key:"..key.."[orderId:"..orderId..",userId:"..userId..",activityId:"..activityId.."]");
redis.call('xadd', key, '*', 'orderId', orderId, 'userId', userId, 'activityId', activityId);
return 0;