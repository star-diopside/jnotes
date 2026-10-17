package jp.gr.java_conf.stardiopside.jnotes.batch.item;

import jp.gr.java_conf.stardiopside.jnotes.batch.value.PostalCodeAddress;
import jp.gr.java_conf.stardiopside.jnotes.data.entity.PostalCode;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;

public class PostalCodeConverter implements ItemProcessor<PostalCodeAddress, PostalCode> {

    @Override
    public @Nullable PostalCode process(PostalCodeAddress item) throws Exception {
        return PostalCode.builder()
                .localGovernmentCode(item.localGovernmentCode())
                .oldPostalCode(item.oldPostalCode())
                .postalCode(item.postalCode())
                .kanaPrefectureName(item.kanaPrefectureName())
                .kanaMunicipalityName(item.kanaMunicipalityName())
                .kanaAreaName(item.kanaAreaName())
                .kanjiPrefectureName(item.kanjiPrefectureName())
                .kanjiMunicipalityName(item.kanjiMunicipalityName())
                .kanjiAreaName(item.kanjiAreaName())
                .build();
    }
}
