package com.syhb.project.responses;
import java.util.Random;

public class TransactionResponse {

    Integer generateStan(){
        return new Random().nextInt(999999);
    }

}
