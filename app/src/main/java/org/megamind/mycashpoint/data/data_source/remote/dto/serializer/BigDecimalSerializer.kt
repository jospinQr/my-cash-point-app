package org.megamind.mycashpoint.data.data_source.remote.dto.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull
import java.math.BigDecimal

object BigDecimalSerializer : KSerializer<BigDecimal> {

    override val descriptor =
        PrimitiveSerialDescriptor("BigDecimal", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: BigDecimal) {
        encoder.encodeString(value.toPlainString())
    }

    override fun deserialize(decoder: Decoder): BigDecimal {
        val jsonDecoder = decoder as? JsonDecoder
            ?: error("BigDecimalSerializer works only with JSON")

        val element = jsonDecoder.decodeJsonElement()

        return when {
            element is JsonPrimitive && element.isString ->
                BigDecimal(element.content)

            element is JsonPrimitive && element.doubleOrNull != null ->
                BigDecimal(element.content)

            else -> error("Invalid BigDecimal value: $element")
        }
    }
}
