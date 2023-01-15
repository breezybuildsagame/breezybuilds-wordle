//
//  GameScene.swift
//  iosApp
//
//  Created by Bradley Phillips on 1/7/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct GameScene: View
{
    @ObservedObject private var handler = GameSceneHandler.shared
    
    var body: some View
    {
        ZStack
        {
            switch(handler.activeView)
            {
                case .GAME:
                    VStack
                    {
                        handler.gameHeader()
                        Spacer()
                        handler.gameBoard()
                        Spacer()
                    }
                default:
                    EmptyView()
            }
        }
        .onAppear { handler.setUp() }
    }
}
