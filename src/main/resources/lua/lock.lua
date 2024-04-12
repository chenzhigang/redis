-- 获取键
local key=KEYS[1]
-- 获取值
local value=ARGV[1]
-- 获取过期时间
local expire=ARGV[2]
-- 执行setNx并设置过期时间操作
local locked = redis.call('set', key, value, 'NX', 'EX', expire)
-- 如果执行成功
if locked then
    -- 返回成功
    return true
else
    -- 返回失败
    return false
end