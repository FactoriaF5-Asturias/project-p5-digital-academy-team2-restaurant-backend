package factoriaf5.team2.goxu;

import org.springframework.boot.SpringApplication;

public class TestGoxuApplication {

	public static void main(String[] args) {
		SpringApplication.from(GoxuApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
