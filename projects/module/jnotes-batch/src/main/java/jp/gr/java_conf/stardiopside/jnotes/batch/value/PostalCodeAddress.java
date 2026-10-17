package jp.gr.java_conf.stardiopside.jnotes.batch.value;

/// 郵便番号住所データ
///
/// @param localGovernmentCode   全国地方公共団体コード
/// @param oldPostalCode         (旧)郵便番号
/// @param postalCode            郵便番号
/// @param kanaPrefectureName    都道府県名(カナ)
/// @param kanaMunicipalityName  市区町村名(カナ)
/// @param kanaAreaName          町域名(カナ)
/// @param kanjiPrefectureName   都道府県名(漢字)
/// @param kanjiMunicipalityName 市区町村名(漢字)
/// @param kanjiAreaName         町域名(漢字)
/// @param flag1                 フラグ(一町域が二以上の郵便番号で表される場合の表示)
/// - 1: 該当
/// - 0: 該当せず
/// @param flag2                 フラグ(小字毎に番地が起番されている町域の表示)
/// - 1: 該当
/// - 0: 該当せず
/// @param flag3                 フラグ(丁目を有する町域の場合の表示)
/// - 1: 該当
/// - 0: 該当せず
/// @param flag4                 フラグ(一つの郵便番号で二以上の町域を表す場合の表示)
/// - 1: 該当
/// - 0: 該当せず
/// @param flag5                 フラグ(更新の表示)
/// - 0: 変更なし
/// - 1: 変更あり
/// - 2: 廃止(廃止データのみ使用)
/// @param flag6                 フラグ(変更理由)
/// - 0: 変更なし
/// - 1: 市政・区政・町政・分区・政令指定都市施行
/// - 2: 住居表示の実施
/// - 3: 区画整理
/// - 4: 郵便区調整等
/// - 5: 訂正
/// - 6: 廃止(廃止データのみ使用)
public record PostalCodeAddress(
        String localGovernmentCode,
        String oldPostalCode,
        String postalCode,
        String kanaPrefectureName,
        String kanaMunicipalityName,
        String kanaAreaName,
        String kanjiPrefectureName,
        String kanjiMunicipalityName,
        String kanjiAreaName,
        String flag1,
        String flag2,
        String flag3,
        String flag4,
        String flag5,
        String flag6) {
}
