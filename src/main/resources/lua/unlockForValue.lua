-- 获取键
local key=KEYS[1]
-- 获取值
local value=ARGV[1]
-- 判断键是否存在，不存在直接返回解锁成功
local flag = redis.call('exists', key)
print(flag)
if flag == 0 then
    return true
end
-- 比较值，如果相同就删除，否则不删除
local oldValue = redis.call('get', key)
if value == oldValue then
    -- 返回成功
    local releaseFlag = redis.call('del', key)
    if releaseFlag then
        return true
    else
        return false
    end
else
    -- 返回失败
    return false
end