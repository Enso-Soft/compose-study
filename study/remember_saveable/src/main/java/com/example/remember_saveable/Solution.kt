package com.example.remember_saveable

import android.os.Parcelable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.parcelize.Parcelize

/**
 * Solution: rememberSaveable로 Configuration Change에서도 상태 유지
 *
 * 이 화면에서는 rememberSaveable을 사용하여 화면 회전에서도
 * 상태를 유지하는 방법을 보여줍니다:
 * 1. 기본 타입 (Int, String, Boolean) 저장
 * 2. @Parcelize로 커스텀 객체 저장
 * 3. listSaver/mapSaver로 커스텀 객체 저장
 */

@Composable
fun SolutionScreen() {
    var selectedDemo by rememberSaveable { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 데모 선택 탭
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedDemo == 0,
                onClick = { selectedDemo = 0 },
                label = { Text("기본 타입") }
            )
            FilterChip(
                selected = selectedDemo == 1,
                onClick = { selectedDemo = 1 },
                label = { Text("Parcelize") }
            )
            FilterChip(
                selected = selectedDemo == 2,
                onClick = { selectedDemo = 2 },
                label = { Text("Saver") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedDemo) {
            0 -> BasicTypesDemo()
            1 -> ParcelizeDemo()
            2 -> SaverDemo()
        }
    }
}

/**
 * 데모 1: 기본 타입 저장
 * Int, String, Boolean 등 Bundle에 저장 가능한 타입
 */
@Composable
fun BasicTypesDemo() {
    // rememberSaveable 사용 - 화면 회전에도 상태 유지!
    var count by rememberSaveable { mutableIntStateOf(0) }
    var text by rememberSaveable { mutableStateOf("") }
    var isChecked by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 핵심 포인트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "핵심 포인트",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "rememberSaveable은 Bundle에 저장 가능한 기본 타입을 " +
                            "자동으로 저장하고 복원합니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "지원 타입: Int, Long, Float, Double, Boolean, String, " +
                            "Parcelable, Serializable 등",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        // 카운터 예시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE8F5E9)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Saveable 카운터",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "$count",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilledTonalButton(onClick = { count-- }) {
                        Text("-1")
                    }
                    Button(onClick = { count++ }) {
                        Text("+1")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "var count by rememberSaveable { mutableStateOf(0) }",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF2E7D32)
                )
            }
        }

        // 텍스트 입력 예시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3F2FD)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Saveable 텍스트 입력",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("메모를 입력하세요") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )
                Text(
                    text = "화면을 회전해도 입력 내용이 유지됩니다!",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF1565C0)
                )
            }
        }

        // 체크박스 예시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF3E5F5)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Saveable 체크박스",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it }
                    )
                    Text("이용약관에 동의합니다")
                }
                Text(
                    text = "상태: ${if (isChecked) "동의함" else "미동의"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF7B1FA2)
                )
            }
        }

        // 코드 비교
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "remember vs rememberSaveable",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "// 화면 회전 시 초기화됨\n" +
                            "var count by remember { mutableStateOf(0) }\n\n" +
                            "// 화면 회전에도 유지됨\n" +
                            "var count by rememberSaveable { mutableStateOf(0) }",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * @Parcelize를 사용한 커스텀 객체 저장
 */
@Parcelize
data class UserInfo(
    val name: String,
    val email: String,
    val age: Int
) : Parcelable

/**
 * 데모 2: @Parcelize 사용
 * 커스텀 data class를 Parcelable로 만들어 저장
 */
@Composable
fun ParcelizeDemo() {
    // @Parcelize가 적용된 객체는 자동으로 저장됨
    var user by rememberSaveable { mutableStateOf(UserInfo("", "", 0)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 핵심 포인트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "@Parcelize 사용법",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "1. build.gradle에 kotlin-parcelize 플러그인 추가\n" +
                            "2. data class에 @Parcelize 어노테이션 추가\n" +
                            "3. Parcelable 인터페이스 구현\n" +
                            "4. rememberSaveable로 사용",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 사용자 정보 입력 폼
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE0F7FA)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "사용자 정보 (Parcelize)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = user.name,
                    onValueChange = { user = user.copy(name = it) },
                    label = { Text("이름") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = user.email,
                    onValueChange = { user = user.copy(email = it) },
                    label = { Text("이메일") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = if (user.age == 0) "" else user.age.toString(),
                    onValueChange = {
                        user = user.copy(age = it.toIntOrNull() ?: 0)
                    },
                    label = { Text("나이") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        // 저장된 데이터 표시
        if (user.name.isNotEmpty() || user.email.isNotEmpty() || user.age > 0) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE8F5E9)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "저장된 UserInfo 객체",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("name: ${user.name}")
                    Text("email: ${user.email}")
                    Text("age: ${user.age}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "화면을 회전해도 객체 전체가 유지됩니다!",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF2E7D32)
                    )
                }
            }
        }

        // 코드 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "코드 예시",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "@Parcelize\n" +
                            "data class UserInfo(\n" +
                            "    val name: String,\n" +
                            "    val email: String,\n" +
                            "    val age: Int\n" +
                            ") : Parcelable\n\n" +
                            "var user by rememberSaveable {\n" +
                            "    mutableStateOf(UserInfo(\"\", \"\", 0))\n" +
                            "}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * listSaver/mapSaver를 사용하지 않는 일반 data class
 */
data class Product(
    val id: Int,
    val name: String,
    val price: Double
)

// listSaver 정의
val ProductListSaver = listSaver<Product, Any>(
    save = { listOf(it.id, it.name, it.price) },
    restore = { Product(it[0] as Int, it[1] as String, it[2] as Double) }
)

// mapSaver 정의
val ProductMapSaver = mapSaver(
    save = { mapOf("id" to it.id, "name" to it.name, "price" to it.price) },
    restore = { Product(it["id"] as Int, it["name"] as String, it["price"] as Double) }
)

/**
 * 데모 3: Saver 사용
 * listSaver, mapSaver를 사용한 커스텀 저장 로직
 */
@Composable
fun SaverDemo() {
    // listSaver 사용
    var productWithList by rememberSaveable(stateSaver = ProductListSaver) {
        mutableStateOf(Product(0, "", 0.0))
    }

    // mapSaver 사용
    var productWithMap by rememberSaveable(stateSaver = ProductMapSaver) {
        mutableStateOf(Product(0, "", 0.0))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 핵심 포인트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Saver 사용법",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Parcelize를 사용할 수 없거나, 저장 로직을 커스터마이즈하고 싶을 때 " +
                            "listSaver 또는 mapSaver를 사용합니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // listSaver 예시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE8EAF6)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "listSaver 사용",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = if (productWithList.id == 0) "" else productWithList.id.toString(),
                    onValueChange = {
                        productWithList = productWithList.copy(id = it.toIntOrNull() ?: 0)
                    },
                    label = { Text("상품 ID") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = productWithList.name,
                    onValueChange = { productWithList = productWithList.copy(name = it) },
                    label = { Text("상품명") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = if (productWithList.price == 0.0) "" else productWithList.price.toString(),
                    onValueChange = {
                        productWithList = productWithList.copy(price = it.toDoubleOrNull() ?: 0.0)
                    },
                    label = { Text("가격") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Text(
                    text = "저장: listOf(id, name, price)\n복원: Product(list[0], list[1], list[2])",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF3949AB)
                )
            }
        }

        // mapSaver 예시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFCE4EC)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "mapSaver 사용",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = if (productWithMap.id == 0) "" else productWithMap.id.toString(),
                    onValueChange = {
                        productWithMap = productWithMap.copy(id = it.toIntOrNull() ?: 0)
                    },
                    label = { Text("상품 ID") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = productWithMap.name,
                    onValueChange = { productWithMap = productWithMap.copy(name = it) },
                    label = { Text("상품명") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = if (productWithMap.price == 0.0) "" else productWithMap.price.toString(),
                    onValueChange = {
                        productWithMap = productWithMap.copy(price = it.toDoubleOrNull() ?: 0.0)
                    },
                    label = { Text("가격") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Text(
                    text = "저장: mapOf(\"id\" to id, \"name\" to name, ...)\n" +
                            "복원: Product(map[\"id\"], map[\"name\"], ...)",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFC2185B)
                )
            }
        }

        // 코드 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "listSaver 정의",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "val ProductSaver = listSaver<Product, Any>(\n" +
                            "    save = { listOf(it.id, it.name, it.price) },\n" +
                            "    restore = { Product(\n" +
                            "        it[0] as Int,\n" +
                            "        it[1] as String,\n" +
                            "        it[2] as Double\n" +
                            "    ) }\n" +
                            ")\n\n" +
                            "var product by rememberSaveable(\n" +
                            "    stateSaver = ProductSaver\n" +
                            ") {\n" +
                            "    mutableStateOf(Product(0, \"\", 0.0))\n" +
                            "}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Saver 선택 가이드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "어떤 방식을 선택할까?",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "@Parcelize: 가장 간단, 권장\n" +
                            "listSaver: 순서가 중요한 간단한 객체\n" +
                            "mapSaver: 키-값으로 명시적 저장 원할 때\n" +
                            "Custom Saver: 복잡한 변환 로직 필요 시",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
