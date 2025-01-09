package guru.qa.niffler.data.entity.user;
import guru.qa.niffler.model.CurrencyValues;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
public class UserEntity implements Serializable {
    private UUID id;
    private String username;
    private CurrencyValues currency;
    private String firstname;
    private String surname;
    private byte[] photo;
    private byte[] photoSmall;
    private String fullname;

    public static UserEntity fromJson(UserEntity json) {
        UserEntity ue = new UserEntity();
        ue.setId(json.id);
        ue.setUsername(json.getUsername());
        ue.setCurrency(json.getCurrency());
        ue.setFirstname(json.getFirstname());
        ue.setSurname(json.getSurname());
        ue.setPhoto(json.getPhoto());
        ue.setPhotoSmall(json.getPhotoSmall());
        ue.setFullname(json.getFullname());
        return ue;
    }
}

