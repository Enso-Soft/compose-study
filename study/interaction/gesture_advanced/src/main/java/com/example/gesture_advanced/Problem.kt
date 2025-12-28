package com.example.gesture_advanced

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

/**
 * 문제 상황 화면
 *
 * 단순 제스처(transformable)만으로는 복잡한 인터랙션을 구현하기 어려움을 보여줍니다.
 *
 * 문제점:
 * 1. 줌 범위 제한이 없음 (무한히 확대/축소 가능)
 * 2. 화면 경계 밖으로 이동 가능
 * 3. 더블탭 줌 불가
 * 4. 플링 애니메이션 없음
 */
@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 상황: 단순 제스처의 한계",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "transformable Modifier만으로 이미지 뷰어를 만들려고 합니다.\n" +
                            "아래 상자를 핀치 줌, 드래그 해보세요.\n" +
                            "무한히 확대/축소되고, 화면 밖으로 사라질 수 있습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 문제 시연 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "transformable만 사용한 경우",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))

                ProblemDemo()
            }
        }

        // 문제점 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "발생하는 문제점",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 줌 범위 제한 불가: scale이 0.001 ~ 무한대까지 가능")
                Text("2. 경계 제한 불가: 상자가 화면 밖으로 완전히 사라질 수 있음")
                Text("3. 더블탭 줌 불가: transformable은 탭을 감지하지 못함")
                Text("4. 플링 불가: 빠르게 스와이프해도 관성 이동 없음")
                Text("5. 회전 각도 제한 불가: 360도 이상 무한 회전 가능")
            }
        }

        // 잘못된 코드 예시
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "제한 없는 코드",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
var scale by remember { mutableFloatStateOf(1f) }
var offset by remember { mutableStateOf(Offset.Zero) }
var rotation by remember { mutableFloatStateOf(0f) }

val state = rememberTransformableState {
    zoomChange, panChange, rotationChange ->
    // 제한 없이 그대로 적용!
    scale *= zoomChange        // 무한 확대/축소
    offset += panChange        // 경계 없음
    rotation += rotationChange // 무한 회전
}

Box(
    modifier = Modifier
        .transformable(state = state)
        .graphicsLayer(
            scaleX = scale,
            scaleY = scale,
            translationX = offset.x,
            translationY = offset.y,
            rotationZ = rotation
        )
)
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 해결이 필요한 이유
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "왜 고급 제스처가 필요한가?",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "실제 앱(갤러리, 지도, 드로잉)에서는:\n" +
                            "- 줌 범위 제한 (0.5x ~ 3x)\n" +
                            "- 화면 경계 내 이동 제한\n" +
                            "- 더블탭으로 빠른 줌\n" +
                            "- 플링으로 자연스러운 스크롤\n" +
                            "이러한 기능이 필수입니다!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }
}

/**
 * 문제 데모: transformable만 사용한 경우
 *
 * 핀치 줌, 드래그, 회전을 해보세요.
 * 무한히 확대/축소되고, 화면 밖으로 사라질 수 있습니다.
 */
@Composable
private fun ProblemDemo() {
    // 제한 없는 상태 변수들
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var rotation by remember { mutableFloatStateOf(0f) }

    val state = rememberTransformableState { zoomChange, panChange, rotationChange ->
        // 문제: 제한 없이 그대로 적용
        scale *= zoomChange
        offset += panChange
        rotation += rotationChange
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // 현재 상태 표시
        Text(
            text = "Scale: %.2f (제한 없음!)".format(scale),
            style = MaterialTheme.typography.bodySmall,
            color = if (scale < 0.3f || scale > 5f) Color.Red else Color.Unspecified
        )
        Text(
            text = "Offset: (%.1f, %.1f)".format(offset.x, offset.y),
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = "Rotation: %.1f".format(rotation),
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 제스처 영역
        Box(
            modifier = Modifier
                .size(250.dp)
                .background(Color.LightGray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .transformable(state = state)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y,
                        rotationZ = rotation
                    )
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Pinch\n& Drag",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "핀치 줌, 드래그, 회전해보세요\n상자가 사라지면 앱을 다시 시작해야 합니다!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
    }
}
