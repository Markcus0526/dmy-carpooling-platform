//
//  Drv_OrderMultiCell.m
//  BJPinChe
//
//  Created by Kimoc on 14-8-18.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "Drv_OrderMultiCell.h"
#import "Drv_OrderMgrViewController.h"

@implementation Drv_OrderMultiCell



- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
    }
    return self;
}



- (void)awakeFromNib
{
    // Initialization code
}


- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];
    
    // Configure the view for the selected state
}


- (void)initData:(STOrderInfo *)data parent:(id)parent
{
	mOrderInfo = data;
	mParent = parent;

	[_lblStartPos setText:mOrderInfo.start_city];
	[_lblEndPos setText:mOrderInfo.end_city];

	// set sex image & change age color according to sex
	//_imgSex
	//if ([mOrderInfo.sex isEqualToString:SEX_MALE]) {

	[_lblCustomerNum setText:[NSString stringWithFormat:@"%d", mOrderInfo.customerNum]];


	if (mOrderInfo.customerNum == 1) {
		[_imgUserPhoto setImageWithURL:[NSURL URLWithString:mOrderInfo.image] placeholderImage:[UIImage imageNamed:@"multi_customer_head.png"]];
	} else {
		[_imgUserPhoto setImage:[UIImage imageNamed:@"multi_customer_head.png"]];
	}


	[_lblPrice setText:[NSString stringWithFormat:@"%.2f%@", mOrderInfo.price, DIAN]];
	[_lblType setText:mOrderInfo.type_desc];
	//[_lblTime setText:[self dateFormatter:mOrderInfo.start_time]];
	[_lblTime setText:mOrderInfo.start_time];
	[_lblContent setText:mOrderInfo.contents];
	[_lblState setText:mOrderInfo.state_desc];

	[_btnOperate setBackgroundImage:nil forState:UIControlStateNormal];
	[_btnOperate setTitle:nil forState:UIControlStateNormal];
	[_btnOperate setHidden:NO];

	switch (mOrderInfo.state) {
		case ORDER_STATE_DRIVER_ACCEPTED:
		case ORDER_STATE_PUBLISHED:
        {
            [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_start.png"] forState:UIControlStateNormal];
            _btnOperate.userInteractionEnabled =YES;
        }
		case ORDER_STATE_GRABBED:
        {
            [_btnOperate setHidden:YES];
//            [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_start.png"] forState:UIControlStateNormal];
//            _btnOperate.userInteractionEnabled =YES;
            break;
        }
        case ORDER_STATE_STARTED:
        {   //执行中 待结算
            [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_BG"] forState:UIControlStateNormal];
            [_btnOperate setTitle:@"执行中" forState:UIControlStateNormal];
            break;
        }
        case ORDER_STATE_DRIVER_ARRIVED:
        {
            [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_BG"] forState:UIControlStateNormal];
            [_btnOperate setTitle:@"执行中" forState:UIControlStateNormal];
            break;
        }
        case ORDER_STATE_PASSENGER_GETON:
        case ORDER_STATE_FINISHED:
        {
			[_btnOperate setHidden:YES];
			[_btnOperate setTitle:@"待 结 算" forState:UIControlStateNormal];
			[_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_BG"] forState:UIControlStateNormal];

			_btnOperate.userInteractionEnabled =NO;
            break;
        }
        case ORDER_STATE_PAYED:
        {
			[_btnOperate setHidden:YES];
//			[_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment.png"] forState:UIControlStateNormal];
//			_btnOperate.userInteractionEnabled =YES;
            break;
        }
        case ORDER_STATE_EVALUATED:
        {
            // set evaluate image by state
            switch (mOrderInfo.evaluated) {
                case EVAL_NOT:
                    [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment.png"] forState:UIControlStateNormal];
                    break;
                case EVAL_HIGH:
                    [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_good.png"] forState:UIControlStateNormal];
                    break;
                case EVAL_MEDIUM:
                    [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_medium.png"] forState:UIControlStateNormal];
                    break;
                case EVAL_LOW:
                    [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_low.png"] forState:UIControlStateNormal];
                    break;
                default:
                    break;
            }
            break;
        }
        case ORDER_STATE_CLOSED:
        {
            
            break;
        }
            
        default:
            break;
    }
    
}
/**
 *  时间格式转换
 *
 *  @param dateString <#dateString description#>
 *
 *  @return <#return value description#>
 */
-(NSString *)dateFormatter:(NSString *)dateString
{
    NSString * year = [dateString componentsSeparatedByString:@"-"][0];
    NSString * month = [dateString componentsSeparatedByString:@"-"][1];
    NSString * day = [([dateString componentsSeparatedByString:@"-"][2]) componentsSeparatedByString:@" "][0];
    NSString * hour = [([dateString componentsSeparatedByString:@" "][1]) componentsSeparatedByString:@":"][0];
    NSString * minute = [([dateString componentsSeparatedByString:@" "][1]) componentsSeparatedByString:@":"][1];
    
    return [NSString stringWithFormat:@"%@年%@月%@日 %@:%@",year,month,day,hour,minute];
}


- (IBAction)onClickedPerformLongOrder:(id)sender
{
    Drv_OrderMgrViewController * ctrl = (Drv_OrderMgrViewController *)mParent;
    [ctrl onClickedPerformLongOrder:mOrderInfo];
}

- (IBAction)onClickedDetailBtn:(id)sender {
    Drv_OrderMgrViewController * ctrl = (Drv_OrderMgrViewController *)mParent;
    [ctrl onClickedPerformInsuranceModal:mOrderInfo];
}

@end
