-- 获取键
local key=KEYS[1]
-- 执行setNx并设置过期时间操作
local flag = redis.call('EXISTS', key)
-- 如果执行成功
if flag then
    -- 返回成功
    return redis.call('DEL', key)
else
    -- 返回失败
    return true
end