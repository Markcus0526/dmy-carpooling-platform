//
//  CPTextViewPlaceholder.h
//  Cassius Pacheco
//
//  Created by Cassius Pacheco on 30/01/13.
//  Copyright (c) 2013 Cassius Pacheco. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CPTextViewPlaceholder : UITextView

@property (nonatomic, strong) NSString *placeholder;

@property (nonatomic) UITextAutocorrectionType originalCorrection;
@property (nonatomic, strong) UIColor *placeholderColor;
@property (nonatomic, strong) UIColor *originalTextColor;
@property (nonatomic, getter = isUsingPlaceholder) BOOL usingPlaceholder;
@property (nonatomic, getter = isSettingPlaceholder) BOOL settingPlaceholder;

@end
