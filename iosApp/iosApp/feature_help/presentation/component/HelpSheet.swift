//
//  HelpSheet.swift
//  iosApp
//
//  Created by Bradley Phillips on 5/3/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct HelpSheet: View
{
    var body: some View
    {
        Text(/*@START_MENU_TOKEN@*/"Hello, World!"/*@END_MENU_TOKEN@*/)
    }
}

extension HelpSheet
{
    struct Tile: View
    {
        @EnvironmentObject private var sceneDimensions: SceneDimensions
        
        var body: some View
        {
            ZStack
            {
            
            }
            .frame(
                width: sceneDimensions.height * (50.0 / idealFrameHeight()),
                height: sceneDimensions.height * (50.0 / idealFrameHeight())
            )
        }
    }
}
