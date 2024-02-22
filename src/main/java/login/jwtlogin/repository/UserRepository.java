package login.jwtlogin.repository;


import login.jwtlogin.model.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<Users,Long> {
    List<Users> findByEmail(String email); // 이메일 넣으면 조회
}
