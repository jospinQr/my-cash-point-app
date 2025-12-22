package org.megamind.mycashpoint.data.data_source.remote.dto.serializer

import kotlinx.serialization.KSerializer
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
        buildClassSerialDescriptor("VolumeByType") {
            element<Map<TransactionType, BigDecimal>>("volumeByType")
        }

    override fun serialize(encoder: Encoder, value: Map<TransactionType, BigDecimal>) {
        val jsonEncoder = encoder as? JsonEncoder
            ?: throw IllegalStateException("This serializer can be used only with JSON format")
        val jsonObject = value.mapKeys { it.key.name }
            .mapValues { it.value.toString() } // Convert BigDecimal en String pour JSON
        jsonEncoder.encodeJsonElement(JsonObject(jsonObject.mapValues {
            it.value.toString().let { v -> JsonPrimitive(v.toDouble()) }
        }))
    }

    override fun deserialize(decoder: Decoder): Map<TransactionType, BigDecimal> {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw IllegalStateException("This serializer can be used only with JSON format")
        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject

        return jsonObject.mapKeys { TransactionType.valueOf(it.key) }
            .mapValues { BigDecimal(it.value.jsonPrimitive.double) }
    }
}