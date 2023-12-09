package com.lcq.composewechat.ui.screen.chat

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lcq.composewechat.CQDivider
import com.lcq.composewechat.R
import com.lcq.composewechat.data.myAvatar
import com.lcq.composewechat.enums.MessageType
import com.lcq.composewechat.models.ChatSession
import com.lcq.composewechat.ui.screen.Loading
import com.lcq.composewechat.utils.autoCloseKeyboard
import com.lcq.composewechat.viewmodel.ChatViewModel
import github.leavesczy.compose_chat.base.utils.TimeUtils.toTalkTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * author: liuchaoqin
 * 创建时间：2023/12/4
 * Describe ：
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(viewModel: ChatViewModel = ChatViewModel(), session: ChatSession) {
    val context = LocalContext.current as Activity
    val scrollState = rememberLazyListState()
    var inputText by remember { mutableStateOf("") }
    // 历史消息
    val lazyChatItems = viewModel.rankChatItems.collectAsLazyPagingItems()
    // 新的消息
    val messageState by viewModel.mMessageFlow.collectAsState()
    val scope = rememberCoroutineScope()

    rememberSystemUiController().setStatusBarColor(Color(ContextCompat.getColor(context, R.color.nav_bg)), darkIcons = true)
    Surface(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .autoCloseKeyboard()
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            session.name,
                            maxLines = 1,
                            fontSize = 16.sp,
                            overflow = TextOverflow.Ellipsis,
                            color = Color(0xff000000)
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                /* doSomething() */
                            }) {
                            Icon(
                                imageVector = Icons.Filled.MoreHoriz,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Color(0xff000000)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = Color(ContextCompat.getColor(context, R.color.nav_bg)),
                        scrolledContainerColor = Color(ContextCompat.getColor(context, R.color.nav_bg)),
                        navigationIconContentColor = Color.White,
                        titleContentColor = Color(ContextCompat.getColor(context, R.color.black_10)),
                        actionIconContentColor = Color(ContextCompat.getColor(context, R.color.black_10)),
                    ),
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                /* doSomething() */
                            }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBackIos,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable {
                                        context.finish()
                                    },
                                tint = Color(0xff000000)
                            )
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar(
                    modifier = Modifier.height(60.dp),
                    containerColor = Color(ContextCompat.getColor(context, R.color.nav_bg)),
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        CQDivider()
                        Row(modifier = Modifier
                            .fillMaxSize()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.BlurCircular,
                                    contentDescription = null,
                                    modifier = Modifier.size(30.dp),
                                    tint = Color(0xff000000)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .padding(6.dp)
                                    .fillMaxHeight()
                                    .weight(6f)
                            ) {
                                OutlinedTextField(
                                    value = inputText,
                                    onValueChange = {
                                        inputText = it
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        containerColor = Color.White,
                                        focusedBorderColor = Color.White,
                                        unfocusedBorderColor = Color.White
                                    ),
                                    textStyle = TextStyle(
                                        fontSize = 14.sp
                                    ),
                                    modifier = Modifier
                                        .padding(0.dp)
                                        .defaultMinSize(minHeight = 45.dp, minWidth = 280.dp)
                                )
                            }
                            if (inputText == "") {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.TagFaces,
                                        contentDescription = null,
                                        modifier = Modifier.size(30.dp),
                                        tint = Color(0xff000000)
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.AddCircleOutline,
                                        contentDescription = null,
                                        modifier = Modifier.size(30.dp),
                                        tint = Color(0xff000000)
                                    )
                                }
                            } else {
                                Box(modifier = Modifier
                                    .padding(
                                        start = 10.dp,
                                        top = 12.dp,
                                        bottom = 12.dp,
                                        end = 10.dp
                                    )
                                    .fillMaxHeight()
                                    .weight(2f),
                                    contentAlignment = Alignment.Center ) {
                                    Text(
                                        text = "发送",
                                        fontSize = 15.sp,
                                        color = Color.White,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xff5ECC71))
                                            .padding(top = 4.dp)
                                            .clickable {
                                                viewModel.sendMessage(inputText, MessageType.SEND)
                                                // 发送信息后滚动到最底部
                                                scope.launch {
                                                    scrollState.scrollToItem(0)
                                                    /**
                                                     * 模拟收到的信息
                                                     */
                                                    delay(100)
                                                    viewModel.sendMessage(
                                                        inputText,
                                                        MessageType.RECEIVE
                                                    )
                                                    inputText = ""
                                                }
                                            },
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            },
            content = { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(ContextCompat.getColor(context, R.color.nav_bg))),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    LazyColumn(
                        state = scrollState,
                        contentPadding = innerPadding,
                        modifier = Modifier.padding(start = 15.dp, end = 15.dp,bottom = 60.dp),
                        reverseLayout = true,
                        verticalArrangement = Arrangement.Top,
                    ) {
                        items(messageState) {
                            MessageItemView(it, session)
                        }

                        items(lazyChatItems) {
                            it?.let {
                                MessageItemView(it, session)
                            }
                        }

                        lazyChatItems.apply {
                            when (loadState.append) {
                                is LoadState.Loading -> {
                                    item { Loading() }
                                } else -> {}
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun MessageItemView(it: ChatSession, session: ChatSession) {
    Box(
        contentAlignment = if (it.messageType == MessageType.RECEIVE) Alignment.CenterStart else Alignment.CenterEnd,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                top = 30.dp,
                start = if (it.messageType == MessageType.RECEIVE) 0.dp else 40.dp,
                end = if (it.messageType == MessageType.SEND) 0.dp else 40.dp
            ),
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
        ) {
            /**
             * 对话时间
             */
            Box(
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                contentAlignment = Alignment.Center
            ) {
               Text(
                   text = toTalkTime(it.createBy),
                   fontSize = 12.sp,
                   color = Color(0xff888888)
               )
            }
            /**
             * 对话信息
             */
            Row(modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
            ) {
                /**
                 * 他人头像（左边）
                 */
                if(it.messageType == MessageType.RECEIVE) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White)
                            .weight(1f)
                    ) {
                        Image(
                            painter = rememberCoilPainter(request = session.avatar),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(4.dp))
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(6f)
                        .wrapContentHeight(),
                    contentAlignment =
                    if (it.messageType == MessageType.RECEIVE) Alignment.TopStart
                    else Alignment.TopEnd
                ) {
                    /**
                     * 尖角
                     */
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        contentAlignment = if (it.messageType == MessageType.RECEIVE) Alignment.TopStart else Alignment.TopEnd
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier
                                .size(20.dp)
                                .rotate(if (it.messageType == MessageType.SEND) 0f else 180f),
                            tint = if (it.messageType == MessageType.RECEIVE) Color.White
                            else Color(0xffA9EA7A)
                        )
                    }
                    /**
                     * 文本内容
                     */
                    Box(
                        modifier = Modifier
                            .padding(
                                start = if (it.messageType == MessageType.RECEIVE) 12.dp else 0.dp,
                                end = if (it.messageType == MessageType.RECEIVE) 0.dp else 12.dp,
                            )
                            .clip(RoundedCornerShape(4.dp))
                            .wrapContentWidth()
                            .wrapContentHeight(Alignment.CenterVertically)
                            .background(
                                if (it.messageType == MessageType.RECEIVE) Color.White else Color(
                                    0xffA9EA7A
                                )
                            ),
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = it.message,
                            fontSize = 16.sp
                        )
                    }
                }
                /**
                 * 本人头像（右边）
                 */
                if(it.messageType == MessageType.SEND) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White)
                            .weight(1f)
                    ) {
                        Image(
                            painter = rememberCoilPainter(request = myAvatar),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(4.dp))
                        )
                    }
                }
            } 
        }
    }
}