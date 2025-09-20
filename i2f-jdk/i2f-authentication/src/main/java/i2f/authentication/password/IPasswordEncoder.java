package i2f.authentication.password;

/**
 * @author Ice2Faith
 * @date 2025/9/20 17:22
 */
public interface IPasswordEncoder {
    String encode(String rawPassword);

    default boolean matches(String rawPassword, String encodedPassword){
        if(rawPassword==null){
            return false;
        }
        if(encodedPassword==null){
            return false;
        }
        String encPassword = encode(rawPassword);
        if(!encodedPassword.equals(encPassword)){
            return false;
        }
        return true;
    }

}
