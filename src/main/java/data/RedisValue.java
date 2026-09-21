package data;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class RedisValue {

    private String value;
    private LocalDate createdAt;
    private long expiryInMilliseconds;

    public RedisValue(String value) {
        this.value = value;
        this.createdAt = LocalDate.now(ZoneId.of("UTC"));
        this.expiryInMilliseconds = -1;
    }

    public RedisValue(String value, int expiryInMilliseconds) {
        this.value = value;
        this.createdAt = LocalDate.now(ZoneId.of("UTC"));

        if(expiryInMilliseconds < 0)
            throw new IllegalArgumentException();
        else
            this.expiryInMilliseconds = expiryInMilliseconds;
    }



    public boolean isExpired() {
        if(this.expiryInMilliseconds == -1)
            return false;
        else
            return ChronoUnit.MILLIS.between(LocalDate.now(ZoneId.of("UTC")), createdAt) > 10;
    }

    public String get() {
        return value;
    }

}
