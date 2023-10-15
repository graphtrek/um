package co.grtk.um.utils.cache;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor
@Data
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class MasterCacheData implements Serializable {
    String masterKey;
    String cache;

}
