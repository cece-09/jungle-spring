package cece.spring.repository;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T, ID> {
    T findByIdOrThrow(ID id, String errorMessage);
}
