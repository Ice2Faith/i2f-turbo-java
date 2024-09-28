package i2f.i18n.provider.impl;

import i2f.i18n.provider.I18nProvider;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2024/9/28 16:12
 */
@Data
@NoArgsConstructor
public class MergedI18nProvider implements I18nProvider {
    protected CopyOnWriteArrayList<I18nProvider> providers = new CopyOnWriteArrayList<>();

    public MergedI18nProvider(List<I18nProvider> providers) {
        this.providers.addAll(providers);
    }

    @Override
    public String get(String lang, String name) {
        if (name == null) {
            return null;
        }
        for (I18nProvider provider : providers) {
            String value = provider.get(lang, name);
            if (value != null) {
                return value;
            }
        }
        return null;
    }
}
