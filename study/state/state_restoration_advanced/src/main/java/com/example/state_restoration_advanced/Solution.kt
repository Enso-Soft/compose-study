package com.example.state_restoration_advanced

import android.os.Parcelable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.parcelize.Parcelize

/**
 * # Solution: 상태 복원 전략
 *
 * ## 해결 방법들
 * 1. @Parcelize 사용 - 가장 간단한 방법
 * 2. mapSaver 사용 - 플랫폼 독립적
 * 3. listSaver 사용 - 더 간결한 코드
 * 4. ViewModel + SavedStateHandle - 프로세스 종료 시에도 복원
 * 5. SaveableStateHolder - 다중 화면 상태 관리
 */

// ===== 방법 1: @Parcelize 사용 =====
@Parcelize
data class UserProfileParcelable(
    val name: String = "",
    val email: String = "",
    val age: Int = 0,
    val hobbies: List<String> = emptyList(),
    val bio: String = ""
) : Parcelable

// ===== 방법 2: mapSaver 사용 =====
data class UserProfileWithMapSaver(
    val name: String = "",
    val email: String = "",
    val age: Int = 0,
    val hobbies: List<String> = emptyList(),
    val bio: String = ""
)

val UserProfileMapSaver: Saver<UserProfileWithMapSaver, Any> = run {
    val nameKey = "name"
    val emailKey = "email"
    val ageKey = "age"
    val hobbiesKey = "hobbies"
    val bioKey = "bio"

    mapSaver(
        save = { profile ->
            mapOf(
                nameKey to profile.name,
                emailKey to profile.email,
                ageKey to profile.age,
                hobbiesKey to ArrayList(profile.hobbies), // ArrayList는 Bundle 저장 가능
                bioKey to profile.bio
            )
        },
        restore = { map ->
            @Suppress("UNCHECKED_CAST")
            UserProfileWithMapSaver(
                name = map[nameKey] as String,
                email = map[emailKey] as String,
                age = map[ageKey] as Int,
                hobbies = (map[hobbiesKey] as? ArrayList<String>) ?: emptyList(),
                bio = map[bioKey] as String
            )
        }
    )
}

// ===== 방법 3: listSaver 사용 =====
data class UserProfileWithListSaver(
    val name: String = "",
    val email: String = "",
    val age: Int = 0,
    val hobbies: List<String> = emptyList(),
    val bio: String = ""
)

val UserProfileListSaver: Saver<UserProfileWithListSaver, Any> = listSaver(
    save = { profile: UserProfileWithListSaver ->
        listOf(
            profile.name,
            profile.email,
            profile.age,
            ArrayList(profile.hobbies),
            profile.bio
        )
    },
    restore = { list ->
        @Suppress("UNCHECKED_CAST")
        UserProfileWithListSaver(
            name = list[0] as String,
            email = list[1] as String,
            age = list[2] as Int,
            hobbies = (list[3] as? ArrayList<String>) ?: emptyList(),
            bio = list[4] as String
        )
    }
)

// ===== 방법 4: ViewModel + SavedStateHandle =====
class ProfileViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // SavedStateHandle의 saveable API 사용
    @OptIn(androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi::class)
    var profile by savedStateHandle.saveable(
        stateSaver = UserProfileMapSaver
    ) {
        mutableStateOf(UserProfileWithMapSaver())
    }
        private set

    fun updateName(name: String) {
        profile = profile.copy(name = name)
    }

    fun updateEmail(email: String) {
        profile = profile.copy(email = email)
    }

    fun updateAge(age: Int) {
        profile = profile.copy(age = age)
    }

    fun updateBio(bio: String) {
        profile = profile.copy(bio = bio)
    }

    fun addHobby(hobby: String) {
        if (hobby.isNotBlank()) {
            profile = profile.copy(hobbies = profile.hobbies + hobby)
        }
    }

    fun removeHobby(index: Int) {
        profile = profile.copy(
            hobbies = profile.hobbies.filterIndexed { i, _ -> i != index }
        )
    }
}

@Composable
fun SolutionScreen() {
    var selectedMethod by remember { mutableIntStateOf(0) }
    val methods = listOf("Parcelize", "mapSaver", "listSaver", "ViewModel")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 방법 선택 탭
        Text(
            text = "Saver 방법 선택",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            methods.forEachIndexed { index, method ->
                FilterChip(
                    selected = selectedMethod == index,
                    onClick = { selectedMethod = index },
                    label = { Text(method, fontSize = 12.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 선택된 방법에 따른 화면
        when (selectedMethod) {
            0 -> ParcelizeSolutionScreen()
            1 -> MapSaverSolutionScreen()
            2 -> ListSaverSolutionScreen()
            3 -> ViewModelSolutionScreen()
        }
    }
}

@Composable
private fun ParcelizeSolutionScreen() {
    // @Parcelize 데이터 클래스는 별도 Saver 없이 바로 사용 가능
    var profile by rememberSaveable {
        mutableStateOf(UserProfileParcelable())
    }
    var newHobby by rememberSaveable { mutableStateOf("") }

    SolutionContent(
        title = "@Parcelize 사용",
        description = "Android 플랫폼 전용. 가장 간단한 방법.",
        codeExample = """
@Parcelize
data class UserProfile(
    val name: String = "",
    val hobbies: List<String> = emptyList()
) : Parcelable

// 별도 Saver 없이 바로 사용 가능!
var profile by rememberSaveable {
    mutableStateOf(UserProfile())
}
        """.trimIndent(),
        profile = ProfileState(
            name = profile.name,
            email = profile.email,
            age = profile.age,
            hobbies = profile.hobbies,
            bio = profile.bio
        ),
        newHobby = newHobby,
        onNameChange = { profile = profile.copy(name = it) },
        onEmailChange = { profile = profile.copy(email = it) },
        onAgeChange = { profile = profile.copy(age = it.toIntOrNull() ?: 0) },
        onBioChange = { profile = profile.copy(bio = it) },
        onNewHobbyChange = { newHobby = it },
        onAddHobby = {
            if (newHobby.isNotBlank()) {
                profile = profile.copy(hobbies = profile.hobbies + newHobby)
                newHobby = ""
            }
        },
        onRemoveHobby = { index ->
            profile = profile.copy(
                hobbies = profile.hobbies.filterIndexed { i, _ -> i != index }
            )
        }
    )
}

@Composable
private fun MapSaverSolutionScreen() {
    // mapSaver를 MutableState와 함께 사용하는 올바른 방법
    // autoSaver를 사용하여 MutableState<T>를 저장
    var profile by rememberSaveable(
        stateSaver = UserProfileMapSaver
    ) {
        mutableStateOf(UserProfileWithMapSaver())
    }
    var newHobby by rememberSaveable { mutableStateOf("") }

    SolutionContent(
        title = "mapSaver 사용",
        description = "플랫폼 독립적. 명시적인 키로 직렬화.",
        codeExample = """
val UserProfileMapSaver = mapSaver(
    save = { profile ->
        mapOf(
            "name" to profile.name,
            "hobbies" to ArrayList(profile.hobbies)
        )
    },
    restore = { map ->
        UserProfile(
            name = map["name"] as String,
            hobbies = map["hobbies"] as List<String>
        )
    }
)

var profile by rememberSaveable(
    stateSaver = UserProfileMapSaver
) { mutableStateOf(UserProfile()) }
        """.trimIndent(),
        profile = ProfileState(
            name = profile.name,
            email = profile.email,
            age = profile.age,
            hobbies = profile.hobbies,
            bio = profile.bio
        ),
        newHobby = newHobby,
        onNameChange = { profile = profile.copy(name = it) },
        onEmailChange = { profile = profile.copy(email = it) },
        onAgeChange = { profile = profile.copy(age = it.toIntOrNull() ?: 0) },
        onBioChange = { profile = profile.copy(bio = it) },
        onNewHobbyChange = { newHobby = it },
        onAddHobby = {
            if (newHobby.isNotBlank()) {
                profile = profile.copy(hobbies = profile.hobbies + newHobby)
                newHobby = ""
            }
        },
        onRemoveHobby = { index ->
            profile = profile.copy(
                hobbies = profile.hobbies.filterIndexed { i, _ -> i != index }
            )
        }
    )
}

@Composable
private fun ListSaverSolutionScreen() {
    var profile by rememberSaveable(
        stateSaver = UserProfileListSaver
    ) {
        mutableStateOf(UserProfileWithListSaver())
    }
    var newHobby by rememberSaveable { mutableStateOf("") }

    SolutionContent(
        title = "listSaver 사용",
        description = "더 간결한 코드. 인덱스 기반 직렬화.",
        codeExample = """
val UserProfileListSaver = listSaver(
    save = { profile ->
        listOf(
            profile.name,
            ArrayList(profile.hobbies)
        )
    },
    restore = { list ->
        UserProfile(
            name = list[0] as String,
            hobbies = list[1] as List<String>
        )
    }
)

var profile by rememberSaveable(
    stateSaver = UserProfileListSaver
) { mutableStateOf(UserProfile()) }
        """.trimIndent(),
        profile = ProfileState(
            name = profile.name,
            email = profile.email,
            age = profile.age,
            hobbies = profile.hobbies,
            bio = profile.bio
        ),
        newHobby = newHobby,
        onNameChange = { profile = profile.copy(name = it) },
        onEmailChange = { profile = profile.copy(email = it) },
        onAgeChange = { profile = profile.copy(age = it.toIntOrNull() ?: 0) },
        onBioChange = { profile = profile.copy(bio = it) },
        onNewHobbyChange = { newHobby = it },
        onAddHobby = {
            if (newHobby.isNotBlank()) {
                profile = profile.copy(hobbies = profile.hobbies + newHobby)
                newHobby = ""
            }
        },
        onRemoveHobby = { index ->
            profile = profile.copy(
                hobbies = profile.hobbies.filterIndexed { i, _ -> i != index }
            )
        }
    )
}

@Composable
private fun ViewModelSolutionScreen() {
    val viewModel: ProfileViewModel = viewModel()
    val profile = viewModel.profile
    var newHobby by rememberSaveable { mutableStateOf("") }

    SolutionContent(
        title = "ViewModel + SavedStateHandle",
        description = "프로세스 종료 시에도 복원. 프로덕션 권장 방식.",
        codeExample = """
class ProfileViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var profile by savedStateHandle.saveable(
        stateSaver = UserProfileMapSaver
    ) {
        mutableStateOf(UserProfile())
    }
        private set

    fun updateName(name: String) {
        profile = profile.copy(name = name)
    }
}

// Composable에서 사용
val viewModel: ProfileViewModel = viewModel()
val profile = viewModel.profile
        """.trimIndent(),
        profile = ProfileState(
            name = profile.name,
            email = profile.email,
            age = profile.age,
            hobbies = profile.hobbies,
            bio = profile.bio
        ),
        newHobby = newHobby,
        onNameChange = { viewModel.updateName(it) },
        onEmailChange = { viewModel.updateEmail(it) },
        onAgeChange = { viewModel.updateAge(it.toIntOrNull() ?: 0) },
        onBioChange = { viewModel.updateBio(it) },
        onNewHobbyChange = { newHobby = it },
        onAddHobby = {
            if (newHobby.isNotBlank()) {
                viewModel.addHobby(newHobby)
                newHobby = ""
            }
        },
        onRemoveHobby = { viewModel.removeHobby(it) }
    )
}

// 공통 상태 홀더
private data class ProfileState(
    val name: String,
    val email: String,
    val age: Int,
    val hobbies: List<String>,
    val bio: String
)

@Composable
private fun SolutionContent(
    title: String,
    description: String,
    codeExample: String,
    profile: ProfileState,
    newHobby: String,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onAgeChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onNewHobbyChange: (String) -> Unit,
    onAddHobby: () -> Unit,
    onRemoveHobby: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 성공 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "화면 회전 및 프로세스 종료 시에도 상태가 유지됩니다!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 코드 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE8F5E9)
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "코드 예시",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = codeExample,
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    fontSize = 10.sp
                )
            }
        }

        HorizontalDivider()

        // 입력 폼
        Text(
            text = "프로필 편집",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = profile.name,
            onValueChange = onNameChange,
            label = { Text("이름") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = profile.email,
            onValueChange = onEmailChange,
            label = { Text("이메일") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = if (profile.age == 0) "" else profile.age.toString(),
            onValueChange = onAgeChange,
            label = { Text("나이") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = profile.bio,
            onValueChange = onBioChange,
            label = { Text("자기소개") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 3
        )

        // 취미
        Text(
            text = "취미 목록",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newHobby,
                onValueChange = onNewHobbyChange,
                label = { Text("새 취미") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Button(onClick = onAddHobby) {
                Text("추가")
            }
        }

        if (profile.hobbies.isNotEmpty()) {
            Card {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    profile.hobbies.forEachIndexed { index, hobby ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("${index + 1}. $hobby")
                            TextButton(onClick = { onRemoveHobby(index) }) {
                                Text("삭제", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }

        // 현재 상태
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "저장된 데이터 (Bundle에 저장됨)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text("이름: ${profile.name.ifBlank { "(미입력)" }}")
                Text("이메일: ${profile.email.ifBlank { "(미입력)" }}")
                Text("나이: ${if (profile.age == 0) "(미입력)" else profile.age}")
                Text("자기소개: ${profile.bio.ifBlank { "(미입력)" }}")
                Text("취미: ${if (profile.hobbies.isEmpty()) "(없음)" else profile.hobbies.joinToString(", ")}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
