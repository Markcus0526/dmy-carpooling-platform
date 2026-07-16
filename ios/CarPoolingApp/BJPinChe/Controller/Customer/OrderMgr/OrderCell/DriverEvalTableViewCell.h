//
//  DriverEvalTableViewCell.h
//  BJPinChe
//
//  Created by CKK on 14-9-11.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
@class STDriverEvalInfo;
@interface DriverEvalTableViewCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UILabel *passName;
@property (weak, nonatomic) IBOutlet UILabel *eval;
@property (weak, nonatomic) IBOutlet UILabel *time;
@property (weak, nonatomic) IBOutlet UILabel *content;

@property (nonatomic,strong) STDriverEvalInfo * model;

@end
