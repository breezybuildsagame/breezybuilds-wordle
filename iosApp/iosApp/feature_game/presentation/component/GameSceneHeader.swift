//
//  GameSceneHeader.swift
//  iosApp
//
//  Created by Bradley Phillips on 1/9/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct GameSceneHeader: View
{
    @EnvironmentObject private var sceneDimensions: SceneDimensions
    
    var body: some View
    {
        VStack
        {
            Spacer()
                .frame(
                    width: sceneDimensions.width * (390.0 / idealFrameWidth()),
                    height: sceneDimensions.height * (46.0 / idealFrameHeight())
                )
        }
    }
}
