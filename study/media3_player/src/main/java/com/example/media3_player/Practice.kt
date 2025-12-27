package com.example.media3_player

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

// 샘플 비디오 URL 목록 (공개 도메인)
private val SAMPLE_VIDEOS = listOf(
    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4" to "Big Buck Bunny",
    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4" to "Elephants Dream",
    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4" to "Tears of Steel"
)

/**
 * 연습 문제 1: 기본 비디오 플레이어 구현
 *
 * 요구사항:
 * - ExoPlayer 인스턴스를 remember로 생성
 * - MediaItem으로 비디오 URL 설정
 * - AndroidView로 PlayerView 표시
 * - DisposableEffect로 release() 처리
 *
 * TODO: 아래 코드를 완성하세요!
 */
@Composable
fun Practice1_BasicPlayer() {
    val context = LocalContext.current.applicationContext
    val lifecycleOwner = LocalLifecycleOwner.current

    val videoUrl = SAMPLE_VIDEOS[0].first

    // TODO: ExoPlayer 인스턴스 생성 (remember 사용)
    // 힌트: remember { ExoPlayer.Builder(context).build().apply { ... } }

    /*
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
            playWhenReady = true
        }
    }
    */

    // TODO: 생명주기 관리 (DisposableEffect 사용)
    // 힌트: onDispose에서 player.release() 호출

    /*
    DisposableEffect(lifecycleOwner, player) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> player.pause()
                Lifecycle.Event.ON_RESUME -> player.play()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            player.release()
        }
    }
    */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "연습 1: 기본 비디오 플레이어",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: ExoPlayer + DisposableEffect로 플레이어 구현",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: AndroidView로 PlayerView 표시
        // 힌트: AndroidView(factory = { PlayerView(it).apply { this.player = player } })

        /*
        Card(modifier = Modifier.fillMaxWidth()) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        this.player = player
                        useController = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }
        */

        // 플레이스홀더
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("비디오 플레이어 자리\n(구현 필요)")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. remember { ExoPlayer.Builder(context).build() }")
                Text("2. player.setMediaItem(MediaItem.fromUri(url))")
                Text("3. player.prepare()")
                Text("4. DisposableEffect의 onDispose에서 release()")
            }
        }
    }
}

/**
 * 연습 문제 2: 커스텀 재생 컨트롤 버튼 구현
 *
 * 요구사항:
 * - PlayerView의 기본 컨트롤러 숨기기 (useController = false)
 * - Compose 버튼으로 Play/Pause 구현
 * - 10초 앞으로/뒤로 Seek 버튼
 * - 현재 재생 상태를 State로 관리
 *
 * TODO: 커스텀 컨트롤을 구현하세요!
 */
@Composable
fun Practice2_CustomControls() {
    val context = LocalContext.current.applicationContext
    val lifecycleOwner = LocalLifecycleOwner.current

    val videoUrl = SAMPLE_VIDEOS[0].first

    // 플레이어 생성 (완성된 코드)
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
        }
    }

    // TODO: 재생 상태를 추적하는 State 추가
    // 힌트: var isPlaying by remember { mutableStateOf(false) }

    /*
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }
    */

    // TODO: Player.Listener로 상태 변화 감지
    // 힌트: DisposableEffect에서 player.addListener 사용

    /*
    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }

            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) {
                    duration = player.duration
                }
            }
        }
        player.addListener(listener)

        onDispose {
            player.removeListener(listener)
        }
    }
    */

    // TODO: 재생 위치 업데이트 (LaunchedEffect)

    /*
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            currentPosition = player.currentPosition
            kotlinx.coroutines.delay(500)
        }
    }
    */

    // 생명주기 관리
    DisposableEffect(lifecycleOwner, player) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> player.pause()
                Lifecycle.Event.ON_RESUME -> {}
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            player.release()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "연습 2: 커스텀 재생 컨트롤",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: Compose 버튼으로 재생 컨트롤 구현",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 비디오 플레이어 (컨트롤러 숨김)
        Card(modifier = Modifier.fillMaxWidth()) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        this.player = player
                        useController = false // 기본 컨트롤러 숨김
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: 재생 상태 표시

        /*
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("재생 중: ${if (isPlaying) "Yes" else "No"}")
                Text("위치: ${formatTime(currentPosition)} / ${formatTime(duration)}")
            }
        }
        */

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("재생 상태: (구현 필요)")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: 커스텀 컨트롤 버튼 Row
        // 힌트: IconButton, FilledIconButton 사용

        /*
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                player.seekTo(maxOf(0, player.currentPosition - 10000))
            }) {
                Icon(Icons.Default.Replay10, contentDescription = "10초 뒤로")
            }

            FilledIconButton(
                onClick = {
                    if (isPlaying) player.pause() else player.play()
                }
            ) {
                Icon(
                    if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "일시정지" else "재생"
                )
            }

            IconButton(onClick = {
                player.seekTo(minOf(player.duration, player.currentPosition + 10000))
            }) {
                Icon(Icons.Default.Forward10, contentDescription = "10초 앞으로")
            }
        }
        */

        // 플레이스홀더 버튼
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(onClick = { /* TODO */ }) {
                Text("-10s")
            }

            Button(onClick = { /* TODO */ }) {
                Text("Play/Pause")
            }

            OutlinedButton(onClick = { /* TODO */ }) {
                Text("+10s")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. Player.Listener의 onIsPlayingChanged 사용")
                Text("2. player.play(), player.pause()")
                Text("3. player.seekTo(position)")
                Text("4. LaunchedEffect로 위치 업데이트")
            }
        }
    }
}

private fun formatTime(millis: Long): String {
    if (millis < 0) return "00:00"
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}

/**
 * 연습 문제 3: 플레이리스트 + 다음/이전 트랙 기능
 *
 * 요구사항:
 * - 여러 비디오로 플레이리스트 구성
 * - setMediaItems()로 플레이리스트 설정
 * - 다음/이전 트랙 버튼 구현
 * - 현재 트랙 인덱스 표시
 *
 * TODO: 플레이리스트 기능을 구현하세요!
 */
@Composable
fun Practice3_Playlist() {
    val context = LocalContext.current.applicationContext
    val lifecycleOwner = LocalLifecycleOwner.current

    // TODO: 플레이어 생성 및 플레이리스트 설정
    // 힌트: player.setMediaItems(listOf(MediaItem.fromUri(url1), ...))

    /*
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItems = SAMPLE_VIDEOS.map { (url, _) ->
                MediaItem.fromUri(url)
            }
            setMediaItems(mediaItems)
            prepare()
        }
    }
    */

    // TODO: 현재 트랙 인덱스 추적
    // 힌트: var currentIndex by remember { mutableIntStateOf(0) }

    /*
    var currentIndex by remember { mutableIntStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    */

    // TODO: Player.Listener로 트랙 변경 감지
    // 힌트: onMediaItemTransition 콜백 사용

    /*
    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                currentIndex = player.currentMediaItemIndex
            }

            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }
        }
        player.addListener(listener)

        onDispose {
            player.removeListener(listener)
        }
    }
    */

    // TODO: 생명주기 관리

    /*
    DisposableEffect(lifecycleOwner, player) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> player.pause()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            player.release()
        }
    }
    */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "연습 3: 플레이리스트",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: 다음/이전 트랙 버튼 구현",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: 비디오 플레이어

        /*
        Card(modifier = Modifier.fillMaxWidth()) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        this.player = player
                        useController = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
        */

        // 플레이스홀더
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("비디오 플레이어 자리\n(구현 필요)")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: 현재 트랙 정보 표시

        /*
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "현재 재생: ${SAMPLE_VIDEOS[currentIndex].second}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text("트랙: ${currentIndex + 1} / ${SAMPLE_VIDEOS.size}")
            }
        }
        */

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("현재 트랙: (구현 필요)")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: 이전/재생/다음 버튼
        // 힌트: player.seekToPreviousMediaItem(), player.seekToNextMediaItem()

        /*
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { player.seekToPreviousMediaItem() },
                enabled = player.hasPreviousMediaItem()
            ) {
                Icon(Icons.Default.SkipPrevious, contentDescription = "이전 트랙")
            }

            FilledIconButton(
                onClick = {
                    if (isPlaying) player.pause() else player.play()
                }
            ) {
                Icon(
                    if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "일시정지" else "재생"
                )
            }

            IconButton(
                onClick = { player.seekToNextMediaItem() },
                enabled = player.hasNextMediaItem()
            ) {
                Icon(Icons.Default.SkipNext, contentDescription = "다음 트랙")
            }
        }
        */

        // 플레이스홀더 버튼
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(onClick = { /* TODO */ }) {
                Text("Prev")
            }

            Button(onClick = { /* TODO */ }) {
                Text("Play")
            }

            OutlinedButton(onClick = { /* TODO */ }) {
                Text("Next")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 플레이리스트 목록
        Text(
            text = "플레이리스트",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(SAMPLE_VIDEOS) { index, (_, title) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                        // TODO: 현재 재생 중인 트랙 하이라이트
                        // containerColor = if (index == currentIndex)
                        //     MaterialTheme.colorScheme.primaryContainer
                        // else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${index + 1}.",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(title)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. player.setMediaItems(listOf(...))")
                Text("2. player.seekToNextMediaItem()")
                Text("3. player.seekToPreviousMediaItem()")
                Text("4. onMediaItemTransition 리스너")
            }
        }
    }
}
