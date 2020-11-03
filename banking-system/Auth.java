package banking;

public class Auth {
   private static Account authUser;

   public static Account getAuthUser() {
      return authUser;
   }

   public static void logIn(Account authUser) {
      Auth.authUser = authUser;
   }

   public static void logOut() {
      Auth.authUser = null;
   }
}
