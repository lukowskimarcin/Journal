package com.journal.cqrses.infrastructure;


import java.util.List;

import com.journal.cqrses.domain.BaseEntity;
import com.journal.cqrses.queries.BaseQuery;
import com.journal.cqrses.queries.QueryHandlerMethod;

public interface QueryDispatcher {
    <T extends BaseQuery> void registerHandler(Class<T> type, QueryHandlerMethod<T> handler);
    <U extends BaseEntity> List<U> send(BaseQuery query);
}
