-- 获取请求键
local key = KEYS[1];
-- 限流量
local limitReq = tonumber(ARGV[1]);
-- 限流时间范围，默认单位：秒，即在规定的时间范围内只允许规定的请求数量
local limitTime = tonumber(ARGV[2]);
-- 当前时间戳
local now = tonumber(ARGV[3]);
-- 随机字符串
local uuid = ARGV[4];

-- 获取规定时间内请求次数
local count = redis.call('zcount', key, now - limitTime, now);

redis.log(redis.LOG_NOTICE, "key:"..key.."count:"..count);

-- 判断请求是否已达上限，zadd命令为写命令，设置值不能采用当前时间戳（redis.call('time')[1]）不确定的值
if count < limitReq then
    -- 未到上限，添加请求
    redis.call('zadd', key, now, uuid);
    return "";
else
    return "次数已达上限，请稍后再试！";
end



