package sample;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by deepak on 8/13/18.
 */
@Getter
@Setter
public class KeyValuePair {
    private String key;
    private Object value;

    public KeyValuePair(){

    }
}
