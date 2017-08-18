package gr.blxbrgld.rabbit.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * Exchange Domain Object
 * @author blxbrgld
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Exchange {

    private String name;
    private String type;
    private boolean durable;
    private boolean autoDelete;
    private boolean delayed;
    private boolean internal;
    private Map<String, Object> arguments;
    private Map<String, Integer> queues;
}
