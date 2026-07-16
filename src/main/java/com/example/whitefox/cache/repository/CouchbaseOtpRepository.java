package com.example.whitefox.cache.repository;

import com.example.whitefox.cache.entity.OtpCacheDoc;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouchbaseOtpRepository extends CouchbaseRepository<OtpCacheDoc, String> {
}
