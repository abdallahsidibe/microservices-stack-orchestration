package net.youssfi.accountservice;

import net.youssfi.accountservice.clients.CustomerRestClient;
import net.youssfi.accountservice.entities.BankAccount;
import net.youssfi.accountservice.enums.AccountType;
import net.youssfi.accountservice.repository.BankAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootApplication
@EnableFeignClients
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountRepository accountRepository, CustomerRestClient customerRestClient){
        return args -> {
            customerRestClient.allCustomers().forEach(c->{
                BankAccount bankAccount1 = new BankAccount(UUID.randomUUID().toString(), Math.random()*80000, LocalDate.now(), "MAD", AccountType.CURRENT_ACCOUNT, null, c.getId());
                BankAccount bankAccount2 = new BankAccount(UUID.randomUUID().toString(), Math.random()*65432, LocalDate.now(), "MAD", AccountType.SAVING_ACCOUNT, null, c.getId());
                accountRepository.save(bankAccount1);
                accountRepository.save(bankAccount2);
            });


        };
    }

}
