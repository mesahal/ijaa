import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestPasswordEncoding {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        String password = "NewUser@123";
        
        // Encode password (like during registration)
        String encodedPassword = encoder.encode(password);
        System.out.println("Original password: " + password);
        System.out.println("Encoded password: " + encodedPassword);
        
        // Verify password (like during signin)
        boolean matches = encoder.matches(password, encodedPassword);
        System.out.println("Password matches: " + matches);
        
        // Test with a different password
        boolean wrongPassword = encoder.matches("WrongPassword", encodedPassword);
        System.out.println("Wrong password matches: " + wrongPassword);
    }
}
