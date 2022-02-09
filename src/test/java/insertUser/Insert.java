package insertUser;

import com.stock.keeper.stockkeeper.domain.User;
import com.stock.keeper.stockkeeper.service.LoginService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Insert {
    @Test
    public void insertUser(){
        LoginService loginService = new LoginService();
        User user =  loginService.checkLogin("user1", "passwrd");

        Assertions.assertEquals("user1", user.getUsr_name());
    }
}
