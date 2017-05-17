package com.github.zhangyanwei.sct.dao.repository;

import com.github.zhangyanwei.sct.model.entity.User;
import com.github.zhangyanwei.sct.model.entity.User.UserStatus;
import com.github.zhangyanwei.sct.model.entity.User_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static java.lang.String.format;

@Repository
public interface UserRepository extends CrudRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findById(Long id);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByIdNumber(String idNumber);

    class UserSpecs {

        public static Specification<User> status(UserStatus status) {
            return (root, query, builder) -> builder.equal(root.get(User_.status), status);
        }

        public static Specification<User> likeName(String name) {
            return (root, query, builder) -> builder.like(root.get(User_.name), format("%%%s%%", name));
        }

        public static Specification<User> likeNickname(String name) {
            return (root, query, builder) -> builder.like(root.get(User_.nickname), format("%%%s%%", name));
        }
    }
}