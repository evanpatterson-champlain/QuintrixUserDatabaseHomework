package com.example.createdb

class MemberData(id_: Int, firstName_: String, lastName_: String, rewards_: Int) {
    var id: Int
    var firstName: String
    var lastName: String
    var rewards: Int
    init {
        id = id_
        firstName = firstName_
        lastName = lastName_
        rewards = rewards_
    }

    fun toStringArray(): Array<String> {
        return listOf<String>(
            id.toString(),
            firstName,
            lastName,
            rewards.toString()
        ).toTypedArray()
    }

    override fun toString(): String {
        return DBHelper.COLUMN_NAMES
            .zip(toStringArray())
            .joinToString { "\n${it.first}: ${it.second}" }
    }

}