package ru.korbit.cecommon.store.impl;

import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import ru.korbit.cecommon.dao.dbimpl.SessionFactoryHolder;
import ru.korbit.cecommon.store.CacheRegion;
import ru.korbit.cecommon.store.CommonStoreHelper;

import java.io.Serializable;
import java.util.Optional;

/**
 * Created by Artur Belogur on 07.11.17.
 */
public abstract class BaseStoreHelper<T> extends SessionFactoryHolder<T> implements CommonStoreHelper<T> {

    @Autowired
    private RedissonClient redissonClient;

    public BaseStoreHelper(Class<T> tClass) {
        super(tClass);
    }

    @Override
    public void addToCache(Object cacheId, Serializable dbId, CacheRegion cacheRegion) {
        redissonClient
                .getLocalCachedMap(cacheRegion.getRegion(), LocalCachedMapOptions.defaults())
                .fastPut(cacheId, dbId);
    }

    @Override
    public Optional<Serializable> getFromCache(Object cacheId, CacheRegion cacheRegion) {
        return Optional.ofNullable((Serializable) redissonClient
                .getLocalCachedMap(cacheRegion.getRegion(), LocalCachedMapOptions.defaults())
                .get(cacheId));
    }

    @Override
    public T addToDb(T obj) {
        return super.save(obj);
    }

    @Override
    public Optional<T> getFromDb(Serializable id) {
        return super.get(id);
    }

    @Override
    public void updateInDb(T obj) {
        super.update(obj);
    }
}
