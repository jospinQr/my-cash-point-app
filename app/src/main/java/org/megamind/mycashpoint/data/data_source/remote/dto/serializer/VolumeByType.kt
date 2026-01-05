package org.megamind.mycashpoint.data.data_source.remote.dto.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.megamind.mycashpoint.domain.model.TransactionType
import java.math.BigDecimal


// Serializer pour Map<TransactionType, BigDecimal>
object VolumeByTypeSerializer : KSerializer<Map<TransactionType, BigDecimal>> {

    override val descriptor: SerialDescriptor =
        MapSerializer(TransactionType.serializer(), BigDecimalSerializer).descriptor

    override fun serialize(encoder: Encoder, value: Map<TransactionType, BigDecimal>) {
        val jsonEncoder = encoder as? JsonEncoder
            ?: throw IllegalStateException("This serializer can be used only with JSON format")

        val jsonMap = value.mapKeys { it.key.name }
            .mapValues {
                // Si l'API attend vraiment un nombre (et non une chaîne), on garde toDouble()
                // Sinon, JsonPrimitive(it.value.toPlainString()) serait plus sûr.
                JsonPrimitive(it.value.toDouble())
            }

        jsonEncoder.encodeJsonElement(JsonObject(jsonMap))
    }

    override fun deserialize(decoder: Decoder): Map<TransactionType, BigDecimal> {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw IllegalStateException("This serializer can be used only with JSON format")

        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject

        return jsonObject.mapKeys { TransactionType.valueOf(it.key) }
            .mapValues {
                val element = it.value.jsonPrimitive
                if (element.isString) {
                    BigDecimal(element.content)
                } else {
                    BigDecimal(element.content) // BigDecimal(String) handles "123.45" correctly
                }
            }
    }
}