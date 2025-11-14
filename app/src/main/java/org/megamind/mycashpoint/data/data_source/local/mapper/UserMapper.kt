package org.megamind.mycashpoint.data.data_source.local.mapper

import org.megamind.mycashpoint.data.data_source.local.entity.UserEntity
import org.megamind.mycashpoint.domain.model.User

fun User.toUserEntity(): UserEntity {

    return UserEntity(
        id = id,
        name = name,
        codeAgence = idAgence,
        role = role
    )

}


fun UserEntity.toUser(): User {
    return User(
        id = id,
        name = name,
        idAgence = codeAgence,
        role = role
    )
}