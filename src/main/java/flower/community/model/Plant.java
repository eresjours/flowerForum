package flower.community.model;

import lombok.Data;

/**
 * @author WsW
 * @version 1.0
 */
@Data
public class Plant {
    private Long id;
    private String name;
    private String avatar;
    private Long userId;
    private Long deviceId;
    private int temperatureMax;
    private int temperatureMin;
    private int humidityMax;
    private int humidityMin;
}
