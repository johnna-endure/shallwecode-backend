package shallwecode.kr.common

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): LocalDateTime {
        // datetime format 에 따라서 분기처리 필요
        val dateTime = decoder.decodeString();
        val instant = Instant.parse(dateTime)

        val ret = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
//        return LocalDateTime.parse(decoder.decodeString(), DateTimeFormatter.ISO_INSTANT) // Instant로 만든후에 변환
        return ret
    }

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        val result = value.format(DateTimeFormatter.ISO_INSTANT)
        encoder.encodeString(result)
    }
}

object LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: LocalDate) {
        val result = value.format(DateTimeFormatter.ISO_LOCAL_DATE)
        encoder.encodeString(result)
    }

}
