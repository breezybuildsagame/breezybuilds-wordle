//
//  XCTestCase+SceneDimensions.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 1/9/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import XCTest

extension XCTestCase
{
    func idealFrame() -> (width: CGFloat, height: CGFloat) { (390, 844) }
    
    func mockFrame() -> (width: CGFloat, height: CGFloat) { (300, 700) }
}
