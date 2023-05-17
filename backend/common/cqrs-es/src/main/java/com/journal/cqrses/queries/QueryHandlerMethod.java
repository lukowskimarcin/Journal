package com.journal.cqrses.queries;

import java.util.List;

import com.journal.cqrses.domain.BaseEntity;

@FunctionalInterface
public interface QueryHandlerMethod<T extends BaseQuery> {
    List<BaseEntity> handle(T query);
}
