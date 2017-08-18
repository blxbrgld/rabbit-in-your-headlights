package gr.blxbrgld.rabbit.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * Queue Domain Object
 * @author blxbrgld
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Queue {

    private String name;
    private String exchangeFrom;
    private String routingKey;
    private boolean durable;
    private boolean exclusive;
    private boolean autoDelete;
    private Map<String, Object> arguments;
    private Integer countOfMessages;
}
