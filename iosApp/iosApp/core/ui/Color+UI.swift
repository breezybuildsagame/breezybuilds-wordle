//
//  Extension+Color.swift
//  iosApp
//
//  Created by Bradley Phillips on 1/9/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

extension Color
{
    static let ui = Color.UI()
    
    struct UI
    {
        let background = Color("color.background")
        let onBackground = Color("color.on_background")
        let primary = Color("color.primary")
        let onPrimary = Color("color.on_primary")
        let secondary = Color("color.secondary")
        let onSecondary = Color("color.on_secondary")
        let tertiary = Color("color.tertiary")
        let onTertiary = Color("color.on_tertiary")
        let surface = Color("color.surface")
        let onSurface = Color("color.on_surface")
        let error = Color("color.error")
    }
}
