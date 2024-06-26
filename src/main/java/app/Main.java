package app;


import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoArgsConstructor
public class Main {
    public static void main(String[] args) {
        log.info("Is a log!");
        System.out.println("Iniial commit");
    }
}