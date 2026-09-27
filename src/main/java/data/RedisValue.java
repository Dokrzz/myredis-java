package data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static java.lang.Math.abs;

public class RedisValue {

    private String value;
    private LocalDateTime createdAt;
    private long expiryInMilliseconds;

    public RedisValue(String value) {
        this.value = value;
        this.createdAt = LocalDateTime.now(ZoneId.of("UTC"));
        this.expiryInMilliseconds = -1;
    }

    public RedisValue(String value, long expiryInMilliseconds) {
        this.value = value;
        this.createdAt = LocalDateTime.now(ZoneId.of("UTC"));

        if(expiryInMilliseconds < 0)
            throw new IllegalArgumentException();
        else
            this.expiryInMilliseconds = expiryInMilliseconds;
    }



    public boolean isExpired() {
        if(this.expiryInMilliseconds == -1) {
            return false;
        }
        else {
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
            ZonedDateTime createdAtZones = createdAt.atZone(ZoneId.of("UTC"));

            System.out.println("Now is : " + now.toString());
            System.out.println("Created is : " + createdAtZones.toString());


            System.out.println("Time difference is " + ChronoUnit.MILLIS.between(now, createdAtZones));
            return abs(ChronoUnit.MILLIS.between(now, createdAtZones)) > expiryInMilliseconds;
        }
    }

    public String get() {
        return value;
    }

}
