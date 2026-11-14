package jp.gr.java_conf.stardiopside.jnotes.value;

import org.jspecify.annotations.Nullable;

import java.nio.file.Path;

/// ダウンロードデータを示すクラス
///
/// @param fileName     ファイル名
/// @param contentType  コンテンツタイプ
/// @param dataTempFile データを保存した一時ファイル
public record DownloadData(
        String fileName,
        @Nullable String contentType,
        Path dataTempFile) {
}
