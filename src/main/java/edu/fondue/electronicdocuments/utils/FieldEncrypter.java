package edu.fondue.electronicdocuments.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

@Component
@RequiredArgsConstructor
public class FieldEncrypter {

    private final Encrypter encrypter;

    public void encrypt(Object[] state, String[] propertyNames, Object entity) {
        ReflectionUtils.doWithFields(entity.getClass(), field -> encryptField(field, state, propertyNames), EncryptionUtils::isFieldEncrypted);
    }

    private void encryptField(Field field, Object[] state, String[] propertyNames) {
        int propertyIndex = EncryptionUtils.getPropertyIndex(field.getName(), propertyNames);
        Object currentValue = state[propertyIndex];
        if (!(currentValue instanceof String)) {
            throw new IllegalStateException("Encrypted annotation was used on a non-String field");
        }
        state[propertyIndex] = encrypter.encrypt(currentValue.toString());
    }
}
