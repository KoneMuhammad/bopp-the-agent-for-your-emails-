package auth

//WHY: the email api could return bad data so i have this filter layer
fun getEmailIds(emailIdList: List<String>): List<String> {
    return emailIdList.filter { it.isNotBlank() && it.matches(Regex("^[a-zA-Z0-9]+$")) }
}
