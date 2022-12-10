package ly.tithe.breezybuilds_wordle

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform