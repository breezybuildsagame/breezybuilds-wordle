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
    @EnvironmentObject private var sceneDimensions: SceneDimensions
    
    @ObservedObject private var handler = GameSceneHandler.shared
    
    init(handler: GameSceneHandler? = nil)
    {
        self.handler = handler ?? self.handler
    }
    
    var body: some View
    {
        ZStack
        {
            switch(handler.activeView)
            {
                case .GAME:
                    ZStack
                    {
                        VStack
                        {
                            handler.gameHeader()
                            Spacer()
                            handler.gameBoard
                            Spacer()
                            handler.gameKeyboard()
                        }
                        if let announcement = handler.gameAnnouncement()
                        {
                            VStack
                            {
                                Spacer()
                                    .frame(height: sceneDimensions.height * (200 / idealFrameHeight()))
                                announcement
                                Spacer()
                            }
                        }
                    }
                default:
                    EmptyView()
            }
        }
        .onAppear { handler.setUp() }
    }
}
